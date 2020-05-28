package jacob.recess.openapi

import sttp.model._

package object purchase {
  type ServerLogic[Eff[_], In, Out, Err] = Function1[In, Eff[Either[Err, Out]]]
  type Http4sLogic[Eff[_], In, Out] = Function1[In, Eff[Either[StatusCode, Out]]]
}
