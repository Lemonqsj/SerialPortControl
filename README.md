# SerialPortControl
安卓平台实现USB转串口通讯

通过PL2303实现USB转串口，然后监听串口，实现数据的实时读写，自己做的时候一定注意你使用的MCU是半双工，单双工还是全双工的，我上面是直接开了一个线程一直去读取数据，这样就会造成一些问题
我所使用的MCU是半双工的，读得时候不能写，写的时候不能读，这种解决方案有两种，加一个互斥量去判断读写，还有一种就是读完再写，写完再读；
