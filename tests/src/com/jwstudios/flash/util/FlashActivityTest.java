package com.jwstudios.flash.util;

import android.test.ActivityInstrumentationTestCase2;
import com.jwstudios.flash.FlashActivity;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.jwstudios.flash.FlashActivityTest \
 * com.jwstudios.flash.tests/android.test.InstrumentationTestRunner
 */
public class FlashActivityTest extends ActivityInstrumentationTestCase2<FlashActivity> {

    public FlashActivityTest() {
        super("com.jwstudios.flash", FlashActivity.class);
    }

}
