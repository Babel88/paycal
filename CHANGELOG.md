# Changelog

Changelog for paycal.

## Unreleased
### No issue

**Minor changes to the factory driven paycal**


[e331bdff439da8f](https://github.com/ghacupha/paycal/commit/e331bdff439da8f) babel88 *2017-09-07 16:20:39*


## v4.5.0.02
### No issue

**Minor updates the factory driven paycal version 4.5.0-LTS**


[06cd417eeca532f](https://github.com/ghacupha/paycal/commit/06cd417eeca532f) babel88 *2017-09-07 16:07:17*


## v4.5.1
### No issue

**Minor updates the factory driven paycal version 4.5.0-LTS**


[d4822d4fadadfb1](https://github.com/ghacupha/paycal/commit/d4822d4fadadfb1) babel88 *2017-09-07 15:37:13*


## v4.5.0-LTS
### No issue

**Set theme jekyll-theme-minimal**


[27e25ee80ee7c11](https://github.com/ghacupha/paycal/commit/27e25ee80ee7c11) Edwin Njeru *2017-09-07 12:32:59*

**last update before finally succumbing to dependency injection**


[abb1552aa05a445](https://github.com/ghacupha/paycal/commit/abb1552aa05a445) babel88 *2017-09-04 12:23:52*

**introducing dependency injection**


[01635a17a241ce3](https://github.com/ghacupha/paycal/commit/01635a17a241ce3) babel88 *2017-09-03 18:59:58*

**deprecated foreign logic classes**


[233ead4995f8a90](https://github.com/ghacupha/paycal/commit/233ead4995f8a90) babel88 *2017-09-03 18:38:04*

**added exclusiveImportedServiceImpl and inclusiveImportedServiceImpl**


[5e319f3e1a8f4e1](https://github.com/ghacupha/paycal/commit/5e319f3e1a8f4e1) babel88 *2017-09-01 14:54:42*

**fixed some errors in the payment parameters causing zero figures in withholding tax and withholding vat**


[ccfc0301d6642cc](https://github.com/ghacupha/paycal/commit/ccfc0301d6642cc) babel88 *2017-09-01 14:33:36*

**fixed some errors in the payment parameters causing zero figures in withholding tax and withholding vat**


[65b6a1a410fa8c8](https://github.com/ghacupha/paycal/commit/65b6a1a410fa8c8) babel88 *2017-08-31 16:25:15*

**final commit for this branch**


[90b44c113176520](https://github.com/ghacupha/paycal/commit/90b44c113176520) babel88 *2017-08-30 10:26:36*

**Adding the PaymentsControllerRunner interface allowing existing base controllers to delegate prepaymentService implementation to the prepaymentDelegate**


[d53d569ffa37474](https://github.com/ghacupha/paycal/commit/d53d569ffa37474) babel88 *2017-08-30 09:01:56*

**Added RentalPaymentController, and a super abstract class runner the paymentControllerRunner**

 * Also added the prepaymentDelegate which contains the algorithm for auto-adjusting the total expenses amount inorder to accomodate changes prepayment amount

[adffe18c967b733](https://github.com/ghacupha/paycal/commit/adffe18c967b733) babel88 *2017-08-29 15:53:50*

**Added withholdingTaxPaymentController and working on RentalPaymentController**


[b2933267c129832](https://github.com/ghacupha/paycal/commit/b2933267c129832) babel88 *2017-08-29 07:18:19*

**Corrected errors in contractorPaymentsController and tested the contractorLogic**


[0b89e936fcf4586](https://github.com/ghacupha/paycal/commit/0b89e936fcf4586) babel88 *2017-08-28 16:15:08*

**Added abstract controller logic which is not working,**

 * The model will be redesigned again in the next commit inorder
 * to allow actual implementation of the interface DefaultBaseController. The methods in the AbstractBaseController could be borrowed and used by inheritance
 * but that where it stops. As much as possible the new controller and logic classes should be stateless

[4c82ef56c75908e](https://github.com/ghacupha/paycal/commit/4c82ef56c75908e) babel88 *2017-08-28 13:05:56*

**trying to add abstract controllers,models & logic inorder to share more code**


[05f5cfc158970a6](https://github.com/ghacupha/paycal/commit/05f5cfc158970a6) babel88 *2017-08-27 13:43:27*

**adding absatract controller, logic and model**


[0de454587cffe09](https://github.com/ghacupha/paycal/commit/0de454587cffe09) babel88 *2017-08-25 15:53:12*

**debugging NPE caused by factories, pending NPE tests and checks**


[6c0c15c5adaae65](https://github.com/ghacupha/paycal/commit/6c0c15c5adaae65) babel88 *2017-08-25 09:42:58*

**created all factories, pending NPE tests and checks**


[2c74ed54eb9b45a](https://github.com/ghacupha/paycal/commit/2c74ed54eb9b45a) babel88 *2017-08-25 08:51:57*

**rearranging initiation to start at factories adding artifacts**


[ba521f91b290e5b](https://github.com/ghacupha/paycal/commit/ba521f91b290e5b) babel88 *2017-08-25 03:13:14*

**rearranging initiation to start at factories**


[6090ad3699d1014](https://github.com/ghacupha/paycal/commit/6090ad3699d1014) babel88 *2017-08-25 03:10:34*

**working on aop oriented undo framework**


[a37f8a2b06beca5](https://github.com/ghacupha/paycal/commit/a37f8a2b06beca5) babel88 *2017-08-22 18:15:26*

**created end points for redo and undo for payment model**


[b1adc9575cedc0b](https://github.com/ghacupha/paycal/commit/b1adc9575cedc0b) babel88 *2017-08-22 16:14:15*

**created payment model and typicalPaymentsController**


[607f206186780d0](https://github.com/ghacupha/paycal/commit/607f206186780d0) babel88 *2017-08-22 08:21:25*

**amended errors for the contractor payment object**


[1c93b0708b6c011](https://github.com/ghacupha/paycal/commit/1c93b0708b6c011) babel88 *2017-08-18 15:32:42*

**amended errors for the contractor payment object**


[e6669a539a00bf5](https://github.com/ghacupha/paycal/commit/e6669a539a00bf5) babel88 *2017-08-18 15:27:34*

**added prepayment logic to all existing logical models**


[a820846fd5d14a8](https://github.com/ghacupha/paycal/commit/a820846fd5d14a8) babel88 *2017-08-18 15:07:02*

**carried out unit tests for the TypicalPayments and the TypicalWithholdingTaxPayments interfaces**


[0b7cbb050dd8f6d](https://github.com/ghacupha/paycal/commit/0b7cbb050dd8f6d) babel88 *2017-08-17 11:42:05*

**migrating the entire codebase to use BigDecimal for precision**


[ba79acab8f2c9f6](https://github.com/ghacupha/paycal/commit/ba79acab8f2c9f6) babel88 *2017-08-14 10:29:12*

**refactoring the factory api**


[bff5dd107ef3e1a](https://github.com/ghacupha/paycal/commit/bff5dd107ef3e1a) babel88 *2017-08-13 17:55:06*

**initial app configurations**


[4173c2f7f62204c](https://github.com/ghacupha/paycal/commit/4173c2f7f62204c) babel88 *2017-08-13 13:36:03*



