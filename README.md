# Third Coast Recorder

A CLI tool to capture Third Coast telemetry to CSV files. An example of collected telemetry data is in this
Jupyter [notebook](https://github.com/jhh/motion/blob/main/notebooks/trajectory/figure-8.ipynb).

## Usage

```shell
$ ./app/build/install/tcr/bin/tcr -h
Usage: tcr [OPTIONS] COMMAND [ARGS]...

Options:
  --generate-completion [bash|zsh|fish]
  -r, --robot-address ADDRESS      Address of robot providing telemetry
  -h, --help                       Show this message and exit

Commands:
  inventory     Print inventory to stdout.
  subscription  Print subscription template to stdout.
  trigger       Trigger command and capture for DURATION milliseconds.
```

### Capture Mode

```text
Usage: tcr capture [OPTIONS]

  Start capture and run until key is pressed.

  Subscription will be read from "subscription.json" by default. Use the
  "subscription" command to create a subscription JSON file for editing.

  Telemetry output is written to "tcr-<TIMESTAMP>.csv".

Options:
  -f, --subscription-from JSON  Specify subscription JSON file.
  -h, --help                    Show this message and exit
```

Set up a telemetry subscription using a generated subscription template:

```shell
$ # 10.27.67.2 is the default address, removing --robot-address(-r) in further examples
$ ./app/build/install/tcr/bin/tcr --robot-address 10.27.67.2 subscription > subscription.json
```

and edit it to select the desired telemetry measurements.

#### Example `subscription.json`

```json
{
  "type": "start",
  "subscription": [
    {
      "itemId": 0,
      "measurementId": "OUTPUT_VOLTAGE"
    },
    {
      "itemId": 0,
      "measurementId": "OUTPUT_PERCENT"
    },
    {
      "itemId": 0,
      "measurementId": "SELECTED_SENSOR_VELOCITY"
    },
    {
      "itemId": 0,
      "measurementId": "BUS_VOLTAGE"
    }
  ]
}
```

You can determine `itemId` for the desired device by inspecting the output of:

```text
$ ./app/build/install/tcr/bin/tcr inventory 
```

#### Example `inventory.json`

```json
{
  "items": [
    {
      "id": 0,
      "type": "org.strykeforce.telemetry.measurable.TalonSRXMeasurable",
      "description": "TalonSRX 1"
    }
  ],
  "measures": ["..."]
}
```


Telemetry output is written to "tcr-TIMESTAMP.csv".

#### Example `tcr-0318-110736.csv`

```text
timestamp,talonsrx_1__output_voltage,talonsrx_1__output_percentage,talonsrx_1__selected_sensor_velocity__pid_0_,talonsrx_1__bus_voltage
1937,3.277859237536657,0.24926686217008798,0.0,13.15
1942,3.277859237536657,0.24926686217008798,0.0,13.15
...
```

Enable the robot and start the telemetry data collection.

```text
./app/build/install/tcr/bin/tcr capture
starting capture, press enter key to stop capture

stopped capture
```
### Trigger and Capture Mode

Set up `subscription.json` the same way as in capture mode above.

```text
$ ./app/build/install/tcr/bin/tcr trigger -h
Usage: tcr trigger [OPTIONS] DURATION

  Trigger command and capture for DURATION milliseconds.

  Subscription will be read from "subscription.json" by default. Use the
  "subscription" command to create a subscription JSON file for editing.

  Telemetry output is written to "tcr-<TIMESTAMP>.csv".

Options:
  -f, --subscription-from JSON  Specify subscription JSON file.
  -h, --help                    Show this message and exit
```

To trigger a command on the robot to start running during telemetry collection create a `NetworkButton` that watches the
top-level Network Tables key `/Trigger`. For example:

```java
var entry=NetworkTableInstance.getDefault().getEntry("Trigger");
        new NetworkButton(entry).whenPressed(new TalonRunCommand(talonSubsystem));
```

Enable the robot and trigger the telemetry data collection. For example to capture for 5000 msec, run:

```text
./app/build/install/tcr/bin/tcr trigger 5000
starting capture
trigger
stopping capture
```
