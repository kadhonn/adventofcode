#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day17;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in17_ex.txt").unwrap();
    let str = fs::read_to_string("in/in17.txt").unwrap();
    day17::day17(&str.trim());
    println!("took {:?}", before.elapsed());
}