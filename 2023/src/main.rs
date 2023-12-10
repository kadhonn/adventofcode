#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day9;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in9_ex.txt").unwrap();
    let str = fs::read_to_string("in/in9.txt").unwrap();
    day9::day9(&str.trim());
    println!("took {:?}", before.elapsed());
}