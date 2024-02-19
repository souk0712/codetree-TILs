fun main(){
    val n = readLine()!!.toInt()
    val dx = intArrayOf(-1,1,0,0);
    val dy = intArrayOf(0,0,-1,1);

    var k = 0
    var x = 0
    var y = 0
    for(i in 0 until n){
        val (dir, num) = readLine()!!.split(" ")
        if(dir=="N"){
            k=1
        } else if(dir=="S"){
            k=0
        }else if(dir=="W"){
            k=2
        }else if(dir=="E"){
            k=3
        }
        for(j in 0 until num.toInt()){
            x += dx[k]
            y += dy[k]
        }
    }

    println("$y $x")
}