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
package com.bitplan.obdii;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.bitplan.can4eve.CANData;
import com.bitplan.can4eve.CANValue.DoubleValue;
import com.bitplan.can4eve.SoftwareVersion;
import com.bitplan.can4eve.Vehicle.State;
import com.bitplan.can4eve.gui.App;
import com.bitplan.can4eve.gui.javafx.CANProperty;
import com.bitplan.can4eve.gui.javafx.CANPropertyManager;
import com.bitplan.obdii.javafx.CANValuePane;
import com.bitplan.obdii.javafx.JFXCanCellStatePlot;
import com.bitplan.obdii.javafx.JFXCanValueHistoryPlot;
import com.bitplan.obdii.javafx.JavaFXDisplay;

import eu.hansolo.medusa.Gauge;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.layout.Region;

/**
 * a Java FX based display for triplet vehicles
 * 
 * @author wf
 *
 */
public class JFXTripletDisplay extends JavaFXDisplay {

  /**
   * construct me
   * 
   * @param app
   * @param softwareVersion
   * @param obdApp
   */
  public JFXTripletDisplay(App app, SoftwareVersion softwareVersion,
      OBDApp obdApp) {
    super(app, softwareVersion, obdApp);
  }

  /**
   * update the given tab with the given region
   * 
   * @param view
   * @param tabId
   * @param region
   */
  private void updateTab(String view, String tabId, Region region) {
    Tab tab=super.getTab(view, tabId);
    if (tab!=null && region != null) {
      tab.setContent(region);
    } else {
      String problem="";
      String delim="";
      if (tab==null) {
        problem+="tab is null";
        delim=", ";
      }
      if (region==null) {
        problem+=delim+"region is null";
      }
      LOGGER.log(Level.SEVERE,String.format("updateTab %s %s: %s",view,tabId,problem));
    }
  }
  
  /**
   * set bindings
   * 
   * @param canProperties
   */
  public void bind(Map<String, ObservableValue<?>> canProperties) {
    this.canProperties = canProperties;
    // bind values by name
    CANValuePane[] canValuePanes = { chargePane, odoPane };
    for (CANValuePane canValuePane : canValuePanes) {
      if (canValuePane != null) {
        for (Entry<String, Gauge> gaugeEntry : canValuePane.getGaugeMap()
            .entrySet()) {
          bind(gaugeEntry.getValue().valueProperty(),
              this.canProperties.get(gaugeEntry.getKey()));
        }
      }
    }
    if (dashBoardPane != null) {
      bind(dashBoardPane.getRpmGauge().valueProperty(),
          this.canProperties.get("RPM"));
      bind(dashBoardPane.rpmMax.valueProperty(),
          this.canProperties.get("RPM-max"));
      bind(dashBoardPane.rpmAvg.valueProperty(),
          this.canProperties.get("RPM-avg"));

      bind(dashBoardPane.getRpmSpeedGauge().valueProperty(),
          this.canProperties.get("RPMSpeed"));
      bind(dashBoardPane.rpmSpeedMax.valueProperty(),
          this.canProperties.get("RPMSpeed-max"));
      bind(dashBoardPane.rpmSpeedAvg.valueProperty(),
          this.canProperties.get("RPMSpeed-avg"));
    }
    if (clockPane != null) {
      ObservableValue<?> vehicleState = this.canProperties.get("vehicleState");
      SimpleLongProperty msecsProperty = (SimpleLongProperty) this.canProperties
          .get("msecs");
      if (vehicleState != null && msecsProperty != null) {
        msecsProperty.addListener((obs, oldValue, newValue) -> super.clockPane
            .updateMsecs(newValue, (State) vehicleState.getValue()));
      }
    }
  }

  /**
   * setup the special parts e.g. history
   * 
   * @param cpm
   * @throws Exception
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void setupSpecial(CANPropertyManager cpm) throws Exception {
    CANData<Double> temperatureData = cpm.getValue("CellTemperature");
    CANProperty<DoubleValue,Double> cellTemperature = (CANProperty<DoubleValue, Double>) temperatureData;
    final JFXCanCellStatePlot cellStatePlot = new JFXCanCellStatePlot(
        "cellTemperature", "cell", "Temperature", cellTemperature, 1.0,
        0.5);
    Platform.runLater(
        () -> updateTab("mainGroup", "Cell Temp", cellStatePlot.getBarChart()));
    cellStatePlot.updateOn(cellTemperature.getUpdateCountProperty());
    
    CANData<Double> voltageData = cpm.getValue("CellVoltage");
    CANProperty<DoubleValue,Double> cellVoltage = (CANProperty<DoubleValue, Double>) voltageData;
    final JFXCanCellStatePlot cellVoltagePlot = new JFXCanCellStatePlot(
        "cellVoltage", "cell", "Voltage", cellVoltage, 0.01, 0.1);
    Platform.runLater(
        () -> updateTab("mainGroup", "Cell Voltage", cellVoltagePlot.getBarChart()));
    cellVoltagePlot.updateOn(cellVoltage.getUpdateCountProperty());
    
    // setup history
    String title = "SOC/RR";
    String xTitle = "time";
    String yTitle = "%/km";
    Map<String, CANProperty> properties = cpm.getCANProperties("SOC", "Range");
    final JFXCanValueHistoryPlot valuePlot = new JFXCanValueHistoryPlot(title,
        xTitle, yTitle, properties);
    Platform.runLater(() -> updateTab(HISTORY_GROUP,"SOC/RR", valuePlot.createLineChart()));
    valuePlot.updateOn(cpm.get("SOC").getUpdateCountProperty());
  }
}
