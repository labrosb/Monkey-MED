    #!/usr/bin/python
    #-------------------------------------------------------------------------------
    # Name:        MCP3002 Measure 5V
    # Purpose:     Measure the 5V Supply of the Raspberry Pi
    #
    # Author:      paulv
    #
    # Created:     22-10-2015
    # Copyright:   (c) paulv 2015
    # Licence:     <your licence>
    #-------------------------------------------------------------------------------

import spidev
import sys
import time
import paho.mqtt.client as mqtt
from getch import getch
from time import sleep

DEBUG = False
vref = 3.3 * 1000 # V-Ref in mV (Vref = VDD for the MCP3002)
resolution = 2**10 # for 10 bits of resolution
calibration = 38 # in mV, to make up for the precision of the components


    # SPI setup
spi_max_speed = 1000000 # 1 MHz (1.2MHz = max for 2V7 ref/supply)
    # reason is that the ADC input cap needs time to get charged to the input level.
CE = 0 # CE0 | CE1, selection of the SPI device

spi = spidev.SpiDev()
spi.open(0,CE) # Open up the communication to the device
spi.max_speed_hz = spi_max_speed

    #
    # create a function that sets the configuration parameters and gets the results
    # from the MCP3002
    #
def read_mcp3002(channel):
    if channel == 0:
          cmd = 0b01100000
    else:
          cmd = 0b01110000

    if DEBUG : print("cmd = ", cmd)

    spi_data = spi.xfer2([cmd,0]) # send hi_byte, low_byte; receive hi_byte, low_byte

    if DEBUG : print("Raw ADC (hi-byte, low_byte) = {}".format(spi_data))

    adc_data = ((spi_data[0] & 3) << 8) + spi_data[1]
    return adc_data

mqttc = mqtt.Client("stable_signals",0)
mqttc.connect("test.mosquitto.org", 1883)
print("START")
mqttc.publish("EMG-DataX","START")
#mqttc.loop(1) #timeout = 1s

try:
    print("MCP3002 Single Ended CH0 Read of the 5V Pi Supply")
    print("SPI max sampling speed = {}".format(spi_max_speed))
    print("V-Ref = {0}, Resolution = {1}".format(vref, resolution))
    print("SPI device = {0}".format(CE))
    print("-----------------------------\n")

    start = time.time()
    
    while True:
        timer = time.time()*1000
        Stimer = int(timer)
        # average readings to get a more stable one
        channeldata_0 = 0
        for x in range(0, 80):
            channeldata_0 += read_mcp3002(0) # get CH0 input
            sleep(0.00001)
        channeldata = channeldata_0 / 80
        print(channeldata)
            
        mqttc.publish("EMG-Data-Mob",channeldata)           




except KeyboardInterrupt: # Ctrl-C
    if DEBUG : print ("Closing SPI channel")
    spi.close()

def main():
    pass

if __name__ == '__main__':
     main()
     
