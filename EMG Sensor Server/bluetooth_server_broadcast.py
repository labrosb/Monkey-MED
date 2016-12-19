
import spidev
import bluetooth
import threading
#import random
from getch import getch
from time import sleep
#from time import gmtime, strftime


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

def read_mcp3002(channel):
    # see datasheet for more information
    # 8 bit control :
    # X, Strt, SGL|!DIFF, ODD|!SIGN, MSBF, X, X, X
    # 0, 1,    1=SGL,     0 = CH0  , 0   , 0, 0, 0 = 96d
    # 0, 1,    1=SGL,     1 = CH1  , 0   , 0, 0, 0 = 112d
    if channel == 0:
        cmd = 0b01100000
    else:
        cmd = 0b01110000

    if DEBUG : print("cmd = ", cmd)

    spi_data = spi.xfer2([cmd,0]) # send hi_byte, low_byte; receive hi_byte, low_byte

    if DEBUG : print("Raw ADC (hi-byte, low_byte) = {}".format(spi_data))

    # receive data range: 000..3FF (10 bits)
    # MSB first: (set control bit in cmd for LSB first)
    # spidata[0] =  X,  X,  X,  X,  X,  0, B9, B8
    # spidata[1] = B7, B6, B5, B4, B3, B2, B1, B0
    # LSB: mask all but B9 & B8, shift to left and add to the MSB
    adc_data = ((spi_data[0] & 3) << 8) + spi_data[1]
    return adc_data


def runServer():
    trackSignal=False
    connect=False
    name = "EMG_broadcast"
    uuid = "00001101-0000-1000-8000-00805F9B34FB"

    server_sock = bluetooth.BluetoothSocket( bluetooth.RFCOMM )
    server_sock.bind(("", bluetooth.PORT_ANY))
    server_sock.listen(1)
    port = server_sock.getsockname()[1]
    bluetooth.advertise_service( server_sock, name, uuid )

    print ("Waiting for connection..")

    connect=True
    while connect:
        client_sock, client_info = server_sock.accept()
        print ("Connection established with device : ",client_info)
        echo = echoThread(client_sock, client_info)
        echo.setDaemon(True)
        echo.start()

    server_sock.close()


class echoThread(threading.Thread):
    
    def __init__ (self,sock,client_info):
        threading.Thread.__init__(self)
        self.sock = sock
        self.client_info = client_info
        
    def run(self):
        try:
            input = self.sock.recv(1024)
            data = (str(input))
            if data == "b'Start Broadcasting'":
                value=0
                trackSignal=True
                while trackSignal:
                    #corrTime=strftime("%H:%M:%S",gmtime())
                    channeldata = read_mcp3002(0) # get CH0 input
                    self.sock.send(str(channeldata))
                    #print (channeldata)
                    #print (corrTime," : -> ",value)
                    sleep(0.001)
        except (IOError, ValueError):
            print (ValueError)
            trackSignal=False
            connect=False
            self.sock.close()
            print (self.client_info, ": Disconnected")
            print ("Restarting...")
            runServer()

runServer()
