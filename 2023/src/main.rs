#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day11;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in11_ex.txt").unwrap();
    let str = fs::read_to_string("in/in11.txt").unwrap();
    day11::day11(&str.trim());
    println!("took {:?}", before.elapsed());
}