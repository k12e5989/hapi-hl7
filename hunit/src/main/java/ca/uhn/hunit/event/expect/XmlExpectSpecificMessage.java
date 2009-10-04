/**
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the
 * specific language governing rights and limitations under the License.
 *
 * The Initial Developer of the Original Code is University Health Network. Copyright (C)
 * 2001.  All Rights Reserved.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * GNU General Public License (the  "GPL"), in which case the provisions of the GPL are
 * applicable instead of those above.  If you wish to allow use of your version of this
 * file only under the terms of the GPL and not to allow others to use your version
 * of this file under the MPL, indicate your decision by deleting  the provisions above
 * and replace  them with the notice and other provisions required by the GPL License.
 * If you do not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the GPL.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ca.uhn.hunit.event.expect;

import ca.uhn.hunit.compare.xml.XmlMessageCompare;
import ca.uhn.hunit.ex.ConfigurationException;
import ca.uhn.hunit.ex.IncorrectMessageReceivedException;
import ca.uhn.hunit.ex.TestFailureException;
import ca.uhn.hunit.msg.AbstractMessage;
import ca.uhn.hunit.xsd.MessageSource;
import ca.uhn.hunit.xsd.XMLExpectSpecificMessage;
import ca.uhn.hunit.iface.TestMessage;
import ca.uhn.hunit.msg.XmlMessageImpl;
import ca.uhn.hunit.test.TestBatteryImpl;
import ca.uhn.hunit.test.TestImpl;
import org.w3c.dom.Document;

/**
 *
 * @author James
 */
public class XmlExpectSpecificMessage extends AbstractXmlExpectMessage {
    private final String myMessageId;
    private final XmlMessageImpl myMessageProvider;

    public XmlExpectSpecificMessage(TestBatteryImpl theBattery, TestImpl theTest, XMLExpectSpecificMessage theConfig) throws ConfigurationException {
        super(theBattery, theTest, theConfig);

		myMessageId = theConfig.getMessageId();
		AbstractMessage messageProvider = getBattery().getMessage(myMessageId);
		if (!(messageProvider instanceof XmlMessageImpl)) {
			throw new ConfigurationException("Message with ID[" + myMessageId + "] is not an HL7 v2 message type so it can not be used with this expect");
		}
		myMessageProvider = (XmlMessageImpl) messageProvider;

    }

    @Override
    protected void validateMessage(TestMessage<Document> parsedMessage) throws TestFailureException {
        TestMessage<Document> expect = myMessageProvider.getTestMessage();
        XmlMessageCompare compare = new XmlMessageCompare();
        compare.compare(expect, parsedMessage);

        System.out.println("+++" + compare.isSame());

        if (compare.isSame() == false) {
            throw new IncorrectMessageReceivedException(getTest(), null, expect, parsedMessage, "Inforrect message received", compare);
        }
    }

}