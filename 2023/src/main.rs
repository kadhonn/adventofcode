#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day22;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in22_ex.txt").unwrap();
    let str = fs::read_to_string("in/in22.txt").unwrap();
    day22::day22(&str.trim());
    println!("took {:?}", before.elapsed());
}