const { exec } = require('child_process')
const { error } = require('console')
const fs = require('fs')

const oldVerifySignature = verifyCommand => {
  exec(`java -jar verify.jar ${verifyCommand}`, (error, stdout, stderr) => {
    if (error) {
      console.error(`failed to execute jar file: ${error.message}`)
      return
    }
    if (stderr) {
      console.error(`java program stderr: ${stderr}`)
      return
    }
  
    const result = stdout.trim()
    console.log(result === 'true' ? true : false)
    return result === 'true' ? true : false
  })
}


const verifySignature = () => {
  return new Promise((resolve, reject) => {
    exec(`java -jar verify/verifyDilithium.jar verify/input.json`, (error, stdout, stderr) => {
      if (error) {
        console.log("hello")
        reject(error);
        return;
      }
      if (stderr) {
        console.log("hi")
        reject(new Error(`Java program stderr: ${stderr}`));
        return;
      }

      const result = stdout.trim();
      resolve(result === 'true'); // Return true or false
    });
  });
};

module.exports = verifySignature 
