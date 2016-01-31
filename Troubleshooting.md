# Matlab runs out of memory when running `javaPlex` #

Depending on the size of your `javaPlex` computations, you may need to increase the maximum Java heap size. This should not be necessary for the examples in the tutorial.

The following command returns your maximum heap size in bytes.
```
>> java.lang.Runtime.getRuntime.maxMemory
ans = 130875392
```

For example, to increase the maximum heap size to 1GB, create a file named java.opts in your Matlab directory which contains the text `-Xmx1000m` and then restart Matlab. For more information, please see this link: http://www.mathworks.com/support/solutions/en/data/1-18I2C/