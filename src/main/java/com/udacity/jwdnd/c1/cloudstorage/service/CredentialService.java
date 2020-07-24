package com.udacity.jwdnd.c1.cloudstorage.service;

import com.udacity.jwdnd.c1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.c1.cloudstorage.model.Credential;
import com.udacity.jwdnd.c1.cloudstorage.model.CredentialForm;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int addCredential(CredentialForm credentialForm)
    {
        Credential newCredential = new Credential();
        newCredential.setUrl(credentialForm.getUrl());
        newCredential.setUsername(credentialForm.getUsername());
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedKey);
        newCredential.setKey(encodedKey);
        newCredential.setPassword(encryptedPassword);
        newCredential.setUserId(credentialForm.getUserId());
        return credentialMapper.insert(newCredential);
    }

    public Credential getCredential(Integer credentialId)
    {
        return credentialMapper.getCredential(credentialId);
    }

    public String decryptPassword(Credential credential)
    {
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }

    public int deleteCredential(Integer credentialId)
    {
        return credentialMapper.deleteCredential(credentialId);
    }

    public int editCredential(Credential credential)
    {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
        return credentialMapper.editCredential(credential);
    }

    public List<Credential> getCredentials(Integer userId)
    {
        return credentialMapper.getAllCredentials(userId);
    }
}
