Recruitment test

A Subscription management system

ITSF needs to rework its subscription management system. We need you to init a project with some very basic features :
We need to persist some information :

type of subscription (FIX, MOBILE, FIBER), date of subscription, id client
a subscription can have one or more options: name (ROAMING, NETFLIX, MUSIC, HD) and a date of subscription.

To manage those subscriptions, we need 3 routes :

One to get all the subscriptions with their options.
One to add an option to an existing subscription.
One to create a subscription

Rules :
Option ROAMING can be only add to a MOBILE subscription
Option NETFLIX can be only add to a FIBRE subscription
Option HD option can only be added if a NETFLIX option already exists on the subscription.
Option MUSIC can be added to MOBILE or FIBER subscriptions
A subscription cannot have the same option twice
What we want to see

The goal of this exercise is to see how you work with the Spring framework, Hibernate, and how you design your application.
The code must respect the requirements.
We will be very attentive to the code quality.
Layering and use of Spring/Hibernate is important, but do not do additional features like UI, caching, security...
Time

No time!
Delivery

You must deliver a functional application.
Prefer using h2 database or docker environments ready to go.
Feel free to use java version according your preference.
