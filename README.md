[![Donate to this project using patreon.com](https://img.shields.io/badge/patreon-donate-yellow.svg)](https://www.patreon.com/matchilling)

# CHUCKNORRIS.IO

JSON API for random [Chuck Norris jokes](https://api.chucknorris.io).

[chucknorris.io](https://api.chucknorris.io) is a free JSON API for hand curated Chuck Norris facts.

Chuck Norris facts are satirical factoids about martial artist and actor Chuck Norris that have become an Internet
phenomenon and as a result have become widespread in popular culture. The 'facts' are normally absurd hyperbolic claims
about Norris' toughness, attitude, virility, sophistication, and masculinity.

Chuck Norris facts have spread around the world, leading not only to translated versions, but also spawning localized
versions mentioning country-specific advertisements and other Internet phenomena. Allusions are also sometimes made to
his use of roundhouse kicks to perform seemingly any task, his large amount of body hair with specific regard to his
beard, and his role in the action television series Walker, Texas Ranger.

## Usage

```sh
// Retrieve a random chuck joke
$ curl --request GET \
       --url 'https://api.chucknorris.io/jokes/random' \
       --header 'accept: (application/json|text/plain)'

// Add an optional `category` parameter to get a random joke from the given category
$ curl --request GET \
       --url 'https://api.chucknorris.io/jokes/random?category=dev' \
       --header 'accept: (application/json|text/plain)'

// Retrieve a list of available categories
$ curl --request GET \
       --url 'https://api.chucknorris.io/jokes/categories' \
       --header 'accept: (application/json|text/plain)'

// Free text search
$ curl --request GET \
       --url 'https://api.chucknorris.io/jokes/search?query={query}' \
       --header 'accept: (application/json|text/plain)'

// Create a new joke
$ curl --request POST \
       --url https://api.chucknorris.io/jokes \
       --header 'accept: application/json' \
       --header 'authorization: Bearer {token}' \
       --header 'content-type: application/json' \
       --data '{
         "categories": [ "dev" ],
         "value": "Everybody thinks the Galaxy Note 7 is explosive. In fact it is only Chuck Norris who tries to send a WhatsApp message with a selfie to his fans." }'

// Update an existing joke
$ curl --request PUT \
       --url https://api.chucknorris.io/jokes/{joke_id} \
       --header 'accept: application/json' \
       --header 'authorization: Bearer {token}' \
       --header 'content-type: application/json' \
       --data '{ "categories": [ "food" ] }'
```

Example response:
```json
{
    "category" : [
        "dev"
    ],
    "icon_url" : "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
    "id"       : "ye0_hnd3rgq68e_pfvsqqg",
    "url"      : "https://api.chucknorris.io/jokes/ye0_hnd3rgq68e_pfvsqqg",
    "value"    : "Chuck Norris can instantiate an abstract class."
}
```

## License

This distribution is covered by the **GNU GENERAL PUBLIC LICENSE**, Version 3, 29 June 2007.

## Support & Contact

Having trouble with this repository? Check out the documentation at the repository's site or contact m@matchilling.com and we’ll help you sort it out.

Happy Coding

:v:
