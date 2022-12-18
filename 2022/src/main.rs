#![allow(dead_code)]

use std::fs;

mod day11;

fn main() {
    // let str = fs::read_to_string("in/in11_ex.txt").unwrap();
    let str = fs::read_to_string("in/in11.txt").unwrap();
    day11::day11_2(&str.trim());
}