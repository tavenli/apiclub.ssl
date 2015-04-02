package cn.apiclub.tool.ssl;

import java.io.File;

import cn.apiclub.tool.ssl.crypto.KeyPairType;
import cn.apiclub.tool.ssl.crypto.KeyStoreType;
import cn.apiclub.tool.ssl.crypto.SignatureType;

public class KeyStoreEntity {

	/**
	 * 默认为 Java Key Store (keytool工具支持的证书类型)
	 */
	private KeyStoreType keyStoreType = KeyStoreType.JKS;
	private KeyPairType keyPairType = KeyPairType.RSA;
	private int keyPairSize = 2048;
	private SignatureType signatureType = SignatureType.SHA256withRSA;
	private int days = 365;
	private String password = Constant.JDK_CERTS_PASSWORD;
	
	//CN
	private String commonName = "apiclub.cn";
	//OU
	private String organisationUnit ="apiclub";
	//O
	private String organisation = "apiclub";
	//L
	private String locality = "CS";
	//ST
	private String state = "HN";
	//C
	private String countryCode = "CN";
	//EMAILADDRESS
	private String email = "tavenli@qq.com";
	
    private File keyStoreFile;
    
    public KeyStoreEntity(){
    	
    }
    
    /**
     * @param keyStoreFilePath 证书文件的绝对路径，例如：/opt/ssl/server.keystore
     */
    public KeyStoreEntity(String keyStoreFilePath){
    	this.keyStoreFile = new File(keyStoreFilePath);
    }
    

	public KeyStoreType getKeyStoreType() {
		return keyStoreType;
	}

	public void setKeyStoreType(KeyStoreType keyStoreType) {
		this.keyStoreType = keyStoreType;
	}

	public KeyPairType getKeyPairType() {
		return keyPairType;
	}

	public void setKeyPairType(KeyPairType keyPairType) {
		this.keyPairType = keyPairType;
	}

	public int getKeyPairSize() {
		return keyPairSize;
	}

	public void setKeyPairSize(int keyPairSize) {
		this.keyPairSize = keyPairSize;
	}

	public SignatureType getSignatureType() {
		return signatureType;
	}

	public void setSignatureType(SignatureType signatureType) {
		this.signatureType = signatureType;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getOrganisationUnit() {
		return organisationUnit;
	}

	public void setOrganisationUnit(String organisationUnit) {
		this.organisationUnit = organisationUnit;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public File getKeyStoreFile() {
		return keyStoreFile;
	}

	public void setKeyStoreFile(File keyStoreFile) {
		this.keyStoreFile = keyStoreFile;
	}
    
    
}
