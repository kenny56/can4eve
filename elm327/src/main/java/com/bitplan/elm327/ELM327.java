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
package com.bitplan.elm327;

/**
 * communication to ELM327 devices
 * see e.g. https://www.sparkfun.com/datasheets/Widgets/ELM327_AT_Commands.pdf
 */
public interface ELM327 extends ELM327Device,Restartable,Debugable {
    // Connection to delegate most of the work to
    public Connection getCon() ;
    public void setCon(Connection con);

    /**
     * reinitialize the communication to the ELM327 chip
     */
    public void reinitCommunication(long timeOutMsecs) throws Exception;

    public void identify() throws Exception;

    public void initOBD2() throws Exception;

    public void initOBD2(long timeOutMsecs) throws Exception;

    public String getCarVoltage();

    public boolean isHeader();

    public void setHeader(boolean header);

    public boolean isLength();

    public void setLength(boolean length);
    
    public boolean isEcho();

    public void setEcho(boolean echo);
        
    public boolean isUsable();
    public String getInfo();
    public boolean isSTN();
    public void halt();
    public boolean isStarted();
}
