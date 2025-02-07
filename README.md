# Digital Signature App using Post Quantum Cryptography

## General

This is my final graduation project at Ha Noi University of Science and Technology, done under the guidance of master Bui Trong Tung.

## Description

This app allow user to generate and store private keys at their personal devices, also they can extract and bring it to another devices.

Not only that, it uses Post Quantum Cryptography for the signing procedure, prevent the attack of Quantum Computer in the future. 

## Solution Design

Here is my keypair storing solution and general flow for this project:

![keystore](images/keystore.png)

![storekey](images/storekey.png)

## Structure

This project consists of two main modules: 
- Android app (in App folder): written in Java
- Web server (in WebServer folder): written in NodeJs

The steps to run each module are provided within their respective folders.

## App Preview

<div class="image-div" style="display: flex; justify-content: space-evenly; margin-bottom: 10px">
  <div style="text-align: center">
    <img src="images/key-manager-screen.png" alt="key manager screen" height="400" border="1px solid black"> 
    <p>Key Manager screen</p>
  </div>
  <div style="text-align: center">
    <img src="images/biometric-authentication.png" alt="key manager screen" height="400" border="1px solid black"> 
    <p>Biometric authentiation required</p>
  </div>
</div>
<div class="image-div" style="display: flex; justify-content: space-evenly;">
  <div style="text-align: center">
    <img src="images/sign-options.png" height="200"  border="1px solid black">
    <p>Sign options</p>
  </div>
  <div style="text-align: center">
    <img src="images/verify-options.png" height="200"  border="1px solid black">
    <p>Verify options</p>
  </div>
</div>



