/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.pieczewski.ingresstool.share;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Wraps the {@link Dialog} returned by {@link GooglePlusUtil#getErrorDialog}
 * so that it can be properly managed by the {@link android.app.Activity}.
 */
public final class GooglePlusErrorDialogFragment extends DialogFragment {

    /**
     * The error code returned by the
     * {@link GooglePlusUtil#checkGooglePlusApp(android.content.Context)} call.
     */
    public static final String ARG_ERROR_CODE = "errorCode";

    /**
     * The request code given when calling {@link android.app.Activity#startActivityForResult}.
     */
    public static final String ARG_REQUEST_CODE = "requestCode";

    /**
     * Creates a {@link DialogFragment}.
     */
    public GooglePlusErrorDialogFragment() {}

    /**
     * Returns a {@link Dialog} created by {@link GooglePlusUtil#getErrorDialog} with the
     * provided errorCode, activity, and request code.
     *
     * @param savedInstanceState Not used.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        return GooglePlayServicesUtil.getErrorDialog(args.getInt(ARG_ERROR_CODE), getActivity(),
                args.getInt(ARG_REQUEST_CODE));
    }
}