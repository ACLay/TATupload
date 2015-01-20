( for i in `seq 1 500`;
do
echo "sms send 01425695563 Hello text a toastie. The purpose of this is to give a not insignificantly long text message to test the system. I would like a cheese, pineapple and nutella toastie, and I am on the third floor of the hartley library. Why are the things just so darn hard, I mean, come on, can't it be just a bit simpler? Anyway, smell ya later!"
done ) | telnet localhost 5554
