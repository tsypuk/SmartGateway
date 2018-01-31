
package ua.in.smartjava.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="BinaryState" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "SetBinaryStateResponse", namespace = "urn:Belkin:service:basicevent:1")
public class SetBinaryStateResponse {

    @XmlElement(name = "BinaryState", required = true)
    protected String binaryState;

    /**
     * Gets the value of the binaryState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBinaryState() {
        return binaryState;
    }

    /**
     * Sets the value of the binaryState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBinaryState(String value) {
        this.binaryState = value;
    }

}
