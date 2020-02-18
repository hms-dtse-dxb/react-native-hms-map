## Configuration

<details>
  <summary>Setting up the Huawei Account in AppGallery</summary>

## Setting up the Huawei Account in AppGallery

During this step, you will create an app in AppGallery Connect (AGC) of HUAWEI Developer. When creating the app, you will need to enter the app name, app category, default language, and signing certificate fingerprint. After the app has been created, you will be able to obtain the basic configurations for the app, for example, the app ID and app secret, which will then be used in subsequent development activities.

- Sign in to HUAWEI Developer and click Console.

![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/48510a34cbb7d21f.png)

- Click the HUAWEI AppGallery card and access AppGallery Connect.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/92245f090d306301.png)

- On the AppGallery Connect page, click My apps.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/72d6a5b833b7e9eb.png)

- On the displayed My apps page, click New.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/19a1f1d9d41d4604.png)

- Enter the App name, select App category (options: App and Game), and select Default language as needed.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/467be1ce516bb4d6.png)

- Upon successful app creation, the App information page will automatically display. There you can find the App ID and App secret that are assigned by the system to your app.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/d04fe040a2d48bc1.png)

</details>

<details>
  <summary>Add Package Name</summary>

## Add Package Name

The developer sets the package name of the created application on the AGC.

#### Manually enter the package name

- Open the previously created application in AGC application management and select the Develop TAB to pop up an entry to manually enter the package name and select manually enter the package name

![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/a75ceef10842de4f.png)

- Fill in the application package name in the input box and click save
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/49e0f1de42e30f63.png)

#### Upload install APK file to add package name

- Open the previously created application in AGC application management and select the Develop TAB. You can choose to upload the package to get the package name.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/1fc3a684c4721dc3.png)

- Or just click Distribute -> Release app -> Version infomation -> Draft-> Software version
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/da4da1115614b99c.png)

- Select Manage package, and then in the pop-up box, select upload
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/3cba368456e3d259.png)

- In the pop-up upload prompt box, select has been generated APK file
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/f2d7aeb69f5eb0fa.png)

- Select save after successful upload
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/e244fac411ef6614.png)

</details>

<details>
  <summary>Generating a Signing Certificate</summary>

## Generating a Signing Certificate

During this step, you will create a new signature file in Android Studio, which will be used for generating the SHA-256 fingerprint for your app.

- In the menu bar of the Android Studio project that you have created, go to Build>Generate Signed Bundle/APK...

![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/f8bb026ba4fe94b8.png)

- On the Generate Signed Bundle or APK page, select APK and click Next.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/bd50bb31841479a5.png)

- If you've already had a signature file, click Choose existing... , select the signature file, and specify Key store password,Key alias, and Key password for it. After completing this, click Next.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/1a586c71bfd6fb61.png)

- If you don't have a signature file, click Create new...
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/a0b6638b84cd7cc8.png)

- Specify relevant information including Key store path, Password, and Key Alias.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/b85f75f9ec37ce3.png)

- After successfully creating the signature file, you will find the signature file information on the automatically displayed Generate Signed Bundle or APK page. Click Next.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/e08df652c9a1eec3.png)

- On the displayed page, select V1 and V2 next to Signature Versions, and then click Finish. You have now created a signed APK (This APK file can be used to upload the generated package name).
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/9ebb01b31d34755f.png)

</details>

<details>
  <summary>Generating a Signing Certificate Fingerprint</summary>

## Generating a Signing Certificate Fingerprint

During this step, you will need to export the SHA-256 fingerprint by using keytool provided by the JDK and signature file.

- Open the command window and access the bin directory where the JDK is installed.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/3133f0cb0cd01718.png)

- Run the following keytool command in the bin directory and enter the password for the signature file key. This password was specified when the signature file was created.

`keytool -list -v -keystore D:\Android\WorkSpcae\HmsDemo\app\HmsDemo.jks`

![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/9bb7db27f9000f5e.png)

- Obtain the SHA-256 fingerprint from the result.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/fd353bdfa7d4dace.png)

</details>

<details>
  <summary>Add fingerprint certificate to AppGallery Connect</summary>

## Add fingerprint certificate to AppGallery Connect

During this step, you will configure the generated SHA-256 fingerprint in AppGallery Connect.

- In AppGallery Connect, click the app that you have created and go to Develop -> Overview.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/ef0476e36ed92d63.png)

- Go to the App information section and enter the SHA-256 fingerprint that has been generated.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/605397b761b09362.png)

- Click √ to save the fingerprint.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/dd253e309f7f3558.png)

Upon completion, the signing certificate fingerprint will take effect immediately.

</details>

<details>
  <summary>Add Maven libraries and plugin</summary>

## Add Maven libraries and plugin

- In Android Studio root-level (project-level) build.gradle file, add rules to include the HUAWEI agcp plugin and HUAWEI Maven repository
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/3eb130c2be6bec9d.png)

```groovy
buildscript {
    repositories {
        // ...

        // Check that you have the following line (if not, add it)
        maven { url 'http://developer.huawei.com/repo/' } // HUAWEI Maven repository
    }
    dependencies {
        // ...

        // Add the following line
        classpath 'com.huawei.agconnect:agcp:1.2.0.300'  // HUAWEI agcp plugin
    }
}

allprojects {
    // ...
    repositories {
        // ...

        // Check that you have the following line (if not, add it)
        maven { url 'http://developer.huawei.com/repo/' } // HUAWEI Maven repository
        // ...
    }
}
```

- In your module (app-level) build.gradle file (usually app/build.gradle), add a line to the bottom of the file.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/c3c44aacf89fe135.png)

```groovy
apply plugin: 'com.android.application'
// Add the following line
apply plugin: 'com.huawei.agconnect'  // HUAWEI agconnect Gradle plugin

android {
    // ...
}

dependencies {
    // ...
}
```

</details>

<details>
  <summary>Configure project signature</summary>

## Configure project signature

- Copy the generated signature file hmsdemo.jks into the app folder and open your module (app-level) build.gradle file (usually app/build.gradle).
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/c095d0fad96d906e.png)

- Add the signature configuration in the android directory of build.gradle file

```groovy
  signingConfigs {
    release {
        storeFile file('HmsDemo.jks')
        keyAlias 'hmsdemo'
        keyPassword '123456'
        storePassword '123456'
        v1SigningEnabled true
        v2SigningEnabled true
    }
}

buildTypes {
    release {
        signingConfig signingConfigs.release
        minifyEnabled false
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
    debug {
        signingConfig signingConfigs.release
        debuggable true
    }
}
```

![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/f4ff63334c45568f.png)

</details>

<details>
  <summary>Sync Project</summary>

## Sync Project

- Click sync now to synchronize the project

![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/5712de347de1206f.png)

- Completed successfully that means successful synchronization, and the integration preparation is completed.
  ![Image description](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/img/c1122fafe226f6c2.png)

</details>
