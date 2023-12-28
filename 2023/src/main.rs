#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day21;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in21_ex.txt").unwrap();
    let str = fs::read_to_string("in/in21.txt").unwrap();
    day21::day21(&str.trim());
    println!("took {:?}", before.elapsed());
}