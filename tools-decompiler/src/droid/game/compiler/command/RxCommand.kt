package droid.game.compiler.command

import com.sun.xml.internal.fastinfoset.util.StringArray
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

object RxCommand{
    fun exec(cmd: String): Observable<String>{
        println(cmd)
        return Observable.create (ObservableOnSubscribe<String>{
            val process = Runtime.getRuntime().exec(cmd)
            val reader = BufferedReader(InputStreamReader(process.inputStream,"GBK"))

            var line = reader.readLine()
            while(line != null){
                if (line != null) {
                    println(line)
                }
                it.onNext(line)
                line = reader.readLine()
            }
            reader.close()
            when(process.waitFor()){
                0 -> it.onComplete()
                else -> it.onError(Exception("exec command[${cmd}] failed"))
            }

        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform())
    }

    fun cmd(cmd: String): List<String>{
        println(cmd.toString())
        val result = ArrayList<String>()
        val process = Runtime.getRuntime().exec(cmd)
        val reader = BufferedReader(InputStreamReader(process.inputStream,"GBK"))

        var line = reader.readLine()
        while(line != null){
            result.add(line)
            line = reader.readLine()
        }
        reader.close()
        when(process.waitFor()){
            0 -> result.add("execute command success")
            else -> result.add("execute command failed")
        }
        return result
    }
}
