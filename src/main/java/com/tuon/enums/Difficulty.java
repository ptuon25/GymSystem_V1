package com.tuon.enums;

public enum Difficulty {

        EASY("Easy"),
        MEDIUM("Medium"),
        HARD("Hard");

        private String label;

        Difficulty(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
}
