//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.17 at 03:24:42 PM CET 
//


package no.api.meteo.jaxb.textlocation.v1_0;

import java.util.ArrayList;
import java.util.List;
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
 *       &lt;sequence>
 *         &lt;element name="meta" type="{}meta_type"/>
 *         &lt;element name="productdescription" type="{}proddesc_type"/>
 *         &lt;element name="time" type="{}time_type" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "meta",
    "productdescription",
    "time"
})
@XmlRootElement(name = "weather")
public class Weather {

    @XmlElement(required = true)
    protected MetaType meta;
    @XmlElement(required = true)
    protected ProddescType productdescription;
    protected List<TimeType> time;

    /**
     * Gets the value of the meta property.
     * 
     * @return
     *     possible object is
     *     {@link MetaType }
     *     
     */
    public MetaType getMeta() {
        return meta;
    }

    /**
     * Sets the value of the meta property.
     * 
     * @param value
     *     allowed object is
     *     {@link MetaType }
     *     
     */
    public void setMeta(MetaType value) {
        this.meta = value;
    }

    /**
     * Gets the value of the productdescription property.
     * 
     * @return
     *     possible object is
     *     {@link ProddescType }
     *     
     */
    public ProddescType getProductdescription() {
        return productdescription;
    }

    /**
     * Sets the value of the productdescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProddescType }
     *     
     */
    public void setProductdescription(ProddescType value) {
        this.productdescription = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the time property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTime().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TimeType }
     * 
     * 
     */
    public List<TimeType> getTime() {
        if (time == null) {
            time = new ArrayList<TimeType>();
        }
        return this.time;
    }

}