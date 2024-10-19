package DOM;

import java.util.Arrays;

import static DOM.EndingOption.LooseGame;
import static DOM.EndingOption.WinGame;

public class Final {

    private EndingOption gameEndType;       //Парамтр окончания игры
    private final char[] winMessage;            //Сообщение при победе
    private final char[] looseMessage;          //Сообщение при проигрыше


    public Final(EndingOption option, char[] win_mes, char[] loose_mes){
        winMessage = win_mes;
        looseMessage = loose_mes;
        gameEndType = WinGame;
    }
    public Final(){
        winMessage = "You WIN!!!".toCharArray();
        looseMessage = "You Loose".toCharArray();
        gameEndType = WinGame;
    }

    //Изменить параметр окончания
    public int changeType(EndingOption option){
        gameEndType = option;
        return 0;
    }
    //Вывести сообщение об завершении игры
    public int outputFinal(){
        if (gameEndType == WinGame)
            System.out.printf(Arrays.toString(winMessage));
        if (gameEndType == LooseGame)
            System.out.printf(Arrays.toString(looseMessage));
        return 0;
    }
}
