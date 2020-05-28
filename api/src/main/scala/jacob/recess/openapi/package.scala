package jacob.recess

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._

package object openapi {
  type Port = Int Refined Interval.Closed[1, 65535]
}
