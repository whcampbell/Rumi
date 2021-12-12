package com.example.rumi;

public class DashConvo {
    String convoHead;
    String convoNext;

    public DashConvo(String head, String next) {
        convoHead = head;
        convoNext = next;
    }

    public String getConvoHead() {
        return convoHead;
    }

    public String getConvoNext() {
        return convoNext;
    }

    public void setConvoHead(String convoHead) {
        this.convoHead = convoHead;
    }

    public void setConvoNext(String convoNext) {
        this.convoNext = convoNext;
    }
}
