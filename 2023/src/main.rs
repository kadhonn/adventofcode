#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day19;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in19_ex.txt").unwrap();
    let str = fs::read_to_string("in/in19.txt").unwrap();
    day19::day19(&str.trim());
    println!("took {:?}", before.elapsed());
}