pub fn day10_1(str: &str) {
    let mut x = 1;
    let mut cycle = 0;
    let mut result = 0;
    for cmd in str.lines() {
        let mut cycles = 1;
        let mut add_x = 0;
        if cmd.starts_with("addx ") {
            cycles = 2;
            let x1 = &cmd[5..];
            add_x = x1.parse().unwrap();
        }
        for _ in 0..cycles {
            cycle += 1;
            if (cycle + 20) % 40 == 0 {
                result += x * cycle;
                println!("cycle: {} tmp: {} new result: {}", cycle, x * cycle, result);
            }
        }
        println!("{add_x}");
        x += add_x;
    }
    println!("{result}");
}

pub fn day10_2(str: &str) {
    let mut x = 1;
    let mut cycle = 0;
    for cmd in str.lines() {
        let mut cycles = 1;
        let mut add_x = 0;
        if cmd.starts_with("addx ") {
            cycles = 2;
            let x1 = &cmd[5..];
            add_x = x1.parse().unwrap();
        }
        for _ in 0..cycles {
            let visible = x - 1 <= cycle % 40 && x + 1 >= cycle % 40;
            print!("{}", if visible {
                "#"
            } else {
                "."
            });
            cycle += 1;
            if cycle % 40 == 0 {
                println!();
            }
        }
        x += add_x;
    }
}