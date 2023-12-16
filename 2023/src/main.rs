#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day15;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in15_ex.txt").unwrap();
    let str = fs::read_to_string("in/in15.txt").unwrap();
    day15::day15(&str.trim());
    println!("took {:?}", before.elapsed());
}