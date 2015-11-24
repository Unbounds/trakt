package com.unbounds.trakt;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.unbounds.trakt.progress.ProgressActivity;

import org.junit.Before;

/**
 * Created by Evelina Vorobyeva on 24/11/15.
 */
public class ProgressActivityEspressoTest extends ActivityInstrumentationTestCase2<ProgressActivity> {

    private ProgressActivity mActivity;

    public ProgressActivityEspressoTest(){
        super(ProgressActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    public void testPlaceACheckMark() {
    }
}
