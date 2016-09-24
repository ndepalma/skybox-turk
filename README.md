# skybox-turk
Skybox-turk is a jarred up set of utilities to perform crowdsourcing research for situated agents. This jar plugin is a packaged version of a plugin from my cognitive architecture. 



# Installation & Build
* Clone the repository skybox-turk from my github.
* Download leiningen and install it [http://leiningen.org/#install]
* Get the access key and secret key from [http://www.aws.amazon.com]
* My Account in top right
** Security Credentials
** Access Key and Secret Key
* Put your access key in src/crowd/turkfrastructure/TurkServer.java:53
* ./turk_build.sh

# Run it 
* Interactive prompt
 * ./jython 
* or if you'd like to load a previously written script you can run:
 * ./jython \<script.py\>
