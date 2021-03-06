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
package com.bitplan.can4eve.gui.javafx;

import java.util.Map;

import com.bitplan.can4eve.gui.Form;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * a generic Panel
 * 
 * @author wf
 *
 */
public class GenericPanel extends GridPane {
  protected Form form;
  public Map<String, GenericControl> controls;

  /**
   * construct me from the given form description
   * 
   * @param form
   */
  public GenericPanel(Stage stage,Form form) {
    this.form = form;
    setHgap(10);
    setVgap(10);
    setPadding(new Insets(20, 150, 10, 10));
    int ypos = 0;
    controls = GenericDialog.getFields(stage,this, form, ypos);
    for (GenericControl control : controls.values()) {
      control.setEditable(false);
    }
  }
}
