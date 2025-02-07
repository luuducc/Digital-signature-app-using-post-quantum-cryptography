# Digital Signature App using Post Quantum Cryptography
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![NodeJS](https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white)
![Express.js](https://img.shields.io/badge/express.js-%23404d59.svg?style=for-the-badge&logo=express&logoColor=%2361DAFB)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)

## General

This is my final graduation project at Ha Noi University of Science and Technology, done under the guidance of master Bui Trong Tung.

## Description

This app allow user to generate and store private keys at their personal devices, also they can extract and bring it to another devices.

Not only that, it uses Post Quantum Cryptography for the signing procedure, prevent the attack of Quantum Computer in the future. 

## Solution Design

Here is my keypair storing solution and general flow for this project:

<img src="images/keystore.png" alt="keystore" width="450px">

<img src="images/storekey.png" alt="storekey" width="450px">

## Structure

This project consists of two main modules: 
- Android app (in App folder): written in Java
- Web server (in WebServer folder): written in NodeJs

The steps to run each module are provided within their respective folders.

## App Preview

|||
|:---:|:---:|
|<img src="images/key-manager-screen.png" alt="key manager screen" height="400" border="1px solid black">|<img src="images/biometric-authentication.png" alt="key manager screen" height="400" border="1px solid black">|
|Key Manager screen|Biometric authentiation required|
|<img src="images/sign-options.png" height="200"  border="1px solid black">|<img src="images/verify-options.png" height="200"  border="1px solid black">|
|Sign options|Verify options|



