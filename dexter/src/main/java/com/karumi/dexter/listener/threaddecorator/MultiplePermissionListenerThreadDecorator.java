/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.dexter.listener.threaddecorator;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.util.List;

/**
 * Decorator to execute the permission updates on a given thread specification
 */
public class MultiplePermissionListenerThreadDecorator implements MultiplePermissionsListener {

  private final MultiplePermissionsListener listener;
  private final ThreadSpec threadSpec;

  public MultiplePermissionListenerThreadDecorator(MultiplePermissionsListener listener,
      ThreadSpec threadSpec) {
    this.threadSpec = threadSpec;
    this.listener = listener;
  }

  /**
   * Decorates de permission listener execution with a given thread spec
   *
   * @param report In detail report with all the permissions that has been denied and granted
   */
  @Override public void onPermissionsChecked(final MultiplePermissionsReport report) {
    threadSpec.execute(new Runnable() {
      @Override public void run() {
        listener.onPermissionsChecked(report);
      }
    });
  }

  /**
   * The method is intended to do some UI modification, therefore the method is not decorated with a
   * thread spec and will execute on the main thread.
   *
   * @param permissions The permissions that has been requested. Collections of values found in
   * {@link android.Manifest.permission}
   * @param token Token used to continue or cancel the permission request process. The permission
   * request process will remain blocked until one of the token methods is called
   */
  @Override public void onPermissionRationaleShouldBeShown(
      final List<PermissionRequest> permissions, final PermissionToken token) {
    listener.onPermissionRationaleShouldBeShown(permissions, token);
  }
}
