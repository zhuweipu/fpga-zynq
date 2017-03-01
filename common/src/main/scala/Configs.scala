package zynq

import cde.{Parameters, Config, CDEMatchError}
import rocketchip._
import rocket._
import coreplex.BuildTiles
import uncore.devices.NTiles
import uncore.tilelink.TLId
import uncore.agents.NAcquireTransactors
import testchipip._
import Chisel._

class WithZynqAdapter extends Config(
  (pname, site, here) => pname match {
    case SerialInterfaceWidth => 32
    case SerialFIFODepth => 16
    case ResetCycles => 10
    case BuildSerialDriver =>
      (p: Parameters) => Module(new SimSerialWrapper(p(SerialInterfaceWidth)))
    case _ => throw new CDEMatchError
  })

class WithSmallCores extends Config(
  (pname, site, here) => pname match {
    case MulDivKey => Some(MulDivConfig())
    case FPUKey => None
    case NTLBEntries => 4
    case BtbKey => BtbParameters(nEntries = 0)
    case NAcquireTransactors => 2
    case _ => throw new CDEMatchError
  },
  knobValues = {
    case "L1D_SETS" => 64
    case "L1D_WAYS" => 1
    case "L1I_SETS" => 64
    case "L1I_WAYS" => 1
    case "L1D_MSHRS" => 0
    case _ => throw new CDEMatchError
  })

class NoBrPred extends Config(
  (key, _, _) => key match {
    case boom.EnableBranchPredictor => false
  }
)

class ZynqConfig extends Config(new WithZynqAdapter ++ new DefaultFPGAConfig)
class ZynqSmallConfig extends Config(new WithSmallCores ++ new ZynqConfig)
class BOOMZynqConfig extends Config(new WithZynqAdapter ++
  new NoBrPred ++ new boom.SmallBOOMConfig ++ new boom.BOOMFPGAConfig)

class WithIntegrationTest extends Config(
  (pname, site, here) => pname match {
    case BuildSerialDriver =>
      (p: Parameters) => Module(new IntegrationTestSerial()(p))
    case BuildTiles => Seq.fill(site(NTiles)) {
      (_reset: Bool, p: Parameters) => Module(new DummyTile()(p.alterPartial({
        case TileId => 0
        case TLId => "L1toL2"
        case NUncachedTileLinkPorts => 1
      })))
    }
    case _ => throw new CDEMatchError
  })

class IntegrationTestConfig extends Config(new WithIntegrationTest ++ new ZynqConfig)
