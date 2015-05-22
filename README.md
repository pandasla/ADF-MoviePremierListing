# ADF-MoviePremierListing
ADF-MoviePremierListing is total client/server web application (based on MVC pattern) based on JavaServer Faces for UI and J2EE for server side.
It has a basic page (similar to my HTML POC) to display movie premier listing showing on next month (MoviePremierPage.jspx).
For fun, I also created improved version which has month selection and filtering functionality (MoviePremierAdvancedPage.jspx).
Since this is full ADF web application, it requires Weblogic webserver to deploy the generated ear under ADF-MoviePremierListing\deploy folder to run the application.

Features:
  It has a table to show the movie title, date, rated, poster, and summary.
  Users can click on the movie title to open a new page and display detailed information about the movie in IMDB.com.
  Users can use Month pull down menu to select desired month to display movie premier listing.
  Users can filter movie listing by rated such as R, PG-13, PG, and G

Following files are key files to review:
For client side:
  MoviePremierAdvancedPage.jspx for JavaServer Faces UI page (ADF-MoviePremierListing\View\public_html folder)
  MoviePremierAdvancedPage.java for backing bean to interact with UI action and event (ADF-MoviePremierListing\View\src\view\backing folder)

For server side:
  AppModuleImpl.java for server side entrance point for client to call web container API (ADF-MoviePremierListing\Model\src\model folder)
  MovieMetaData.java for server manage bean that has main business logic (ADF-MoviePremierListing\Model\src\model folder)
