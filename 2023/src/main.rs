#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day10;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in10_ex.txt").unwrap();
    let str = fs::read_to_string("in/in10.txt").unwrap();
    day10::day10(&str.trim());
    println!("took {:?}", before.elapsed());
}