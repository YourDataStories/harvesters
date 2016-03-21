package objects;

/**
 * @author G. Razis
 */
public class LegalEntity {

	private boolean valid = false;
	private boolean physicalPerson = false;
	private String name = null;
	private String vatNumber = null;
	private String countryCode = null;
	private String addressName = null;
	private String addressNumber = null;
	private String postalCode = null;
	private String addressRegion = null;

	public LegalEntity(boolean valid, boolean physicalPerson, String name, String vatNumber, String countryCode, String addressName, 
					   String addressNumber, String postalCode, String addressRegion) {
		
		this.valid = valid;
		this.physicalPerson = physicalPerson;
		this.name = name;
		this.vatNumber = vatNumber;
		this.countryCode = countryCode; 
		this.addressName = addressName;
		this.addressNumber = addressNumber;
		this.postalCode = postalCode;
		this.addressRegion = addressRegion;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isPhysicalPerson() {
		return physicalPerson;
	}

	public void setPhysicalPerson(boolean physicalPerson) {
		this.physicalPerson = physicalPerson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getAddressNumber() {
		return addressNumber;
	}

	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAddressRegion() {
		return addressRegion;
	}

	public void setAddressRegion(String addressRegion) {
		this.addressRegion = addressRegion;
	}

}