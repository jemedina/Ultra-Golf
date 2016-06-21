package com.uaa.ultragolf.Global;

public class Info {
    private int bolasRestantes;
    public Info() {
        reset();
    }

    private void reset() {
        bolasRestantes = 3;
    }

    public boolean gameOver() {
        return bolasRestantes < 0;
    }
    public void setBolas(int b){
        this.bolasRestantes = b;
    }
    public int getBolasRestantes() {
        return bolasRestantes;
    }

    public void decrementaBolas(){
        bolasRestantes--;
    }
}
