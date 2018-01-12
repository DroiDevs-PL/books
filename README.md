# Books (working name)

## About Project
Books is an android application that allow user to store information about books they owe or they've read. 
It will allow to save basic book data like title, authors, category along with description or summary 
created by the user and personal rank.

In the future it will allow users to share their libraries with friends and people nearby. They will be able to easily find
and read reviews of book that would like to buy or read or even to borrow book from friends.

## For who
This application is for all of you that read a lot of books and want to easily store key thoughts about yout readings.
The idea of this application belongs to @MichaelB-pl whose wife loves to read.

## About us
![alt text](https://s3-us-west-2.amazonaws.com/udacity-email/Scholarships/Google-Dev-EMEA-Badge.png?utm_medium=email&utm_campaign=sch_2017-10-30_google-developer-challenge-scholarship_accepted&utm_source=blueshift&utm_content=sch_2017-10-30_google-developer-challenge-scholarship_accepted&bsft_eid=6ebfb890-4e58-4d0c-89c4-90026f3275c2&bsft_clkid=fb16baff-6f2b-4022-9f2c-6521de43c254&bsft_uid=1a927a7a-7e26-4087-ab7f-668094bf6608&bsft_mid=84ee8ae6-13af-4529-95c3-4fe10cc6cea3&bsft_txnid=b6078c50-c84e-49f5-a8bf-45b7e71f1ad2)
We are group of developers connected by **Google Developer Challenge Scholarship - Android Developer track**. 
We decided to work together to create great open-sourced app and learn from each other.

### Team
[Natalia Jastrzębska](https://github.com/nani92)

[Karol Lisiewicz](https://github.com/klisiewicz)

[Michał Bachta](https://github.com/MichaelB-pl)

[Adam Świderski]()

## Our goal
As we mentioned our main goal is to learn new thing. That is why we pay a lot of attention to our architecture. 
We use **MVVM** with **Android Architecture Components**, **LiveData**, **Room**. Most of the time we use [google samples](https://github.com/googlesamples/android-architecture-components) as our guide.

## MVP (Minimum Viable Product)
We decided that our MVP will be *offline*(without backend) app containing following features:

### Create Library
This screen allows user to create his library by providing a name. In future it will be transformed to Login Screen.

### Add book
This screen allows user to add new book by providing link to cover image, title, authors, category and description.

### Display list of books (Library Screen)
It's main screen of app where whole library is displayed: book cover along with title and authors.

### Display book details
In this screen user can preview more details of book such as: category, description.

### Edit book
This screen allows user to edit book data.

### Remove
Option of removing book is available in **Libarary Screen** by swiping book and in **Edit screen**.

### Searching book
Option of searching book is available in **Library Screen** in toolbar.

### Filtering
Option of filtering books is available in **Library Screen** by using dropdown menu.

### Importing books & Exporting books
App allows users to export whole library to CSV file and import library from CSV. It's needed mostly while app doesnt store anything in cloud.




