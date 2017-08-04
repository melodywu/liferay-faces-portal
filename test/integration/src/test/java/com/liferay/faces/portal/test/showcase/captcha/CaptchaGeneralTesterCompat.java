/**
 * Copyright (c) 2000-2017 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.liferay.faces.portal.test.showcase.captcha;

import org.junit.FixMethodOrder;
import org.junit.Test;

import org.junit.runners.MethodSorters;

import com.liferay.faces.portal.test.integration.PortalTestUtil;
import com.liferay.faces.test.selenium.browser.BrowserDriver;
import com.liferay.faces.test.selenium.browser.TestUtil;
import com.liferay.faces.test.selenium.browser.WaitingAsserter;
import com.liferay.faces.test.showcase.inputtext.InputTextTester;


/**
 * This class provides a compatibility layer for different versions of Liferay
 *
 * @author  Vernon Singleton
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class CaptchaGeneralTesterCompat extends InputTextTester {

	// Protected Constants
	protected static final String CAPTCHA_INPUT_XPATH = "//input[contains(@id,'captchaText')]";
	protected static final String CAPTCHA_MSG_ERROR_XPATH = "//tr[contains(@class,'portlet-msg-error')]";

	@Test
	public void runCaptchaGeneralTest_C_Authenticated_CaptchaMaxChallengesException() throws Exception {

		BrowserDriver browserDriver = getBrowserDriver();
		WaitingAsserter waitingAsserter = getWaitingAsserter();

		try {

			// 1. Click on the "Sign-In" link and enter the email address and password of a valid user.
			TestUtil.signIn(browserDriver);
			navigateToUseCase(browserDriver, "captcha", "general");
			browserDriver.clickElement(requiredCheckbox1Xpath);

			// 2. Enter an incorrect value into the *Captcha* text field, meaning a value that is different than the one
			// that is displayed in the distorted captcha image.
			String correctCaptchaValue = getCorrectCaptchaValue(browserDriver);
			browserDriver.clearElement(CAPTCHA_INPUT_XPATH);
			browserDriver.sendKeysToElement(CAPTCHA_INPUT_XPATH, correctCaptchaValue + "1234");

			// 3. Click on the *Submit* button and verify that an error message is displayed indicating that an
			// incorrect value was submitted.
			browserDriver.clickElementAndWaitForRerender(submitButton1Xpath);
			waitingAsserter.assertElementDisplayed(CAPTCHA_MSG_ERROR_XPATH);
			waitingAsserter.assertTextPresentInElement("Text verification failed", CAPTCHA_MSG_ERROR_XPATH);

			// 4. Click on the *Submit* button again and verify that an error message is displayed indicating
			// that the maximum number of attempts has been exceeded.
			browserDriver.clickElementAndWaitForRerender(submitButton1Xpath);
			waitingAsserter.assertElementDisplayed(CAPTCHA_MSG_ERROR_XPATH);
			waitingAsserter.assertTextPresentInElement("Maximum number of CAPTCHA attempts exceeded",
				CAPTCHA_MSG_ERROR_XPATH);

			// 5. Repeat step #4.
			browserDriver.clickElementAndWaitForRerender(submitButton1Xpath);
			waitingAsserter.assertElementDisplayed(CAPTCHA_MSG_ERROR_XPATH);
			waitingAsserter.assertTextPresentInElement("Maximum number of CAPTCHA attempts exceeded",
				CAPTCHA_MSG_ERROR_XPATH);
		}
		finally {

			// 6. Click on the "Sign-Out" link.
			PortalTestUtil.signOut(browserDriver);
		}
	}

	protected abstract String getCorrectCaptchaValue(BrowserDriver browserDriver);
}
