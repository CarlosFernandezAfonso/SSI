package utils;


import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;
import java.util.*;
public class CertValidator {
    
    public CertValidator(){
        
    }
    
    public boolean validarCertificados(String pathCA, String pathCE) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, Exception{
        
        PKIXParameters params = createParams(pathCA);
        CertPath cp = null;
        cp = createPath(pathCE);

        System.out.println("path: " + cp);
        CertPathValidator cpv = CertPathValidator.getInstance("PKIX");
        try{
            CertPathValidatorResult cpvr = cpv.validate(cp, params);
        }
        catch(CertPathValidatorException e){
            return false;
        }
        
        return true;
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length == 0)
            throw new Exception("must specify at least trustAnchor");
        PKIXParameters params = createParams(args[0]);
        CertPath cp = null;
        if (args.length == 2 && (args[1].endsWith("pkcs7") || args[1].endsWith("cer"))) {
            cp = createPath(args[1]);
        } else {
            cp = createPath(args);
        }
        System.out.println("path: " + cp);
        CertPathValidator cpv = CertPathValidator.getInstance("PKIX");
        CertPathValidatorResult cpvr = cpv.validate(cp, params);
        System.out.println(cpvr);
    }
    public static PKIXParameters createParams(String anchorFile) throws Exception {
        TrustAnchor anchor = new TrustAnchor(getCertFromFile(anchorFile), null);
        Set anchors = Collections.singleton(anchor);
        PKIXParameters params = new PKIXParameters(anchors);
        params.setRevocationEnabled(false);
        return params;
    }
    public static CertPath createPath(String certPath) throws Exception {
        File certPathFile = new File(certPath);
        FileInputStream certPathInputStream = new FileInputStream(certPathFile);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
    try {
            return cf.generateCertPath(certPathInputStream, "PKCS7");
    } catch (CertificateException ce) {
        // try generateCertificates
                      Collection c = cf.generateCertificates(certPathInputStream);
        return cf.generateCertPath(new ArrayList(c));
    }
    }
    public static CertPath createPath(String[] certs) throws Exception {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
        List list = new ArrayList();
        for (int i = 1; i < certs.length; i++) {
            list.add(getCertFromFile(certs[i]));
        }
    CertPath cp = cf.generateCertPath(list);
    return cp;
    }
    /**
     * Get a DER or BASE64-encoded X.509 certificate from a file.
     *
     * @param certFilePath path to file containing DER or BASE64-encoded certificate
     * @return X509Certificate
     * @throws Exception on error
     */
    public static X509Certificate getCertFromFile(String certFilePath)
        throws Exception {
        X509Certificate cert = null;
        File certFile = new File(certFilePath);
        FileInputStream certFileInputStream = new FileInputStream(certFile);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        cert = (X509Certificate) cf.generateCertificate(certFileInputStream);
        return cert;
    }
}