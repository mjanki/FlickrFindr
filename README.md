# FlickrFindr

* Search for photos through the Flickr API
* Display 25 photos at a time (thumbnail and title)
* Load more 25 more photos everytime you scroll to the bottom (Paging)
* Tap on photo or title to display full image
* Ability to Save photos and display it offline
* Error Handling (API errors, no results found)
* Tests are included for the Errors only (but code was written to be testable)
* Search Terms are recorded and will autocomplete for future searches
* Code is  modular
* Java Files are PhotoFragment in the View module and PhotoStorageDao in the Storage module
* Room, Retrofit, RxJava (RxKotlin), LiveData, DiffUtil were used among other things
* The Database is the source of truth
