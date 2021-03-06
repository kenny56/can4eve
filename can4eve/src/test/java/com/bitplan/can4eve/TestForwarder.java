/**
 *
 * This file is part of the https://github.com/BITPlan/can4eve open source project
 *
 * Copyright 2017 BITPlan GmbH https://github.com/BITPlan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *
 *  http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bitplan.can4eve;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.bitplan.elm327.Config;
import com.bitplan.elm327.Config.ConfigMode;

/**
 * Created by wf on 05.06.17.
 */

public class TestForwarder {
  @Test
  public void testCan4EveForwarding() throws FileNotFoundException {
    Config config = Config.getInstance(ConfigMode.Test);
    if (config != null) {
      Can4eve can4eve = new Can4eve();
      String args[] = { "-f", "-d", "--device", config.getSerialDevice() };
      can4eve.maininstance(args);
    }
  }
  
  @Test
  public void testOBDIITester() throws FileNotFoundException {
    Config config = Config.getInstance(ConfigMode.Test);
    if (config != null) {
      Can4eve can4eve = new Can4eve();
      String args[] = { "-t", "-d", "--device", config.getSerialDevice() };
      can4eve.maininstance(args);
    }
  }
}
