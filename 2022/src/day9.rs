use std::collections::HashSet;

pub fn day9_1(str: &str) {
    let mut head_x: i32 = 0;
    let mut head_y: i32 = 0;
    let mut tail_x: i32 = 0;
    let mut tail_y: i32 = 0;

    let mut visited = HashSet::new();

    for line in str.lines() {
        let mut split = line.split(" ");
        let dir = split.next().unwrap();
        let steps: i32 = split.next().unwrap().parse().unwrap();

        for _ in 0..steps {
            match dir {
                "R" => {
                    head_x += 1;
                }
                "L" => {
                    head_x -= 1;
                }
                "U" => {
                    head_y -= 1;
                }
                "D" => {
                    head_y += 1;
                }
                other => {
                    panic!("unknown direction {}", other);
                }
            }
            let x_diff = head_x - tail_x;
            let y_diff = head_y - tail_y;
            if x_diff.abs() > 1 || y_diff.abs() > 1 {
                tail_x += x_diff.signum();
                tail_y += y_diff.signum();
            }
            visited.insert((tail_x, tail_y));
        }
    }

    println!("{}", visited.len());
}

#[derive(Copy, Clone)]
struct Node {
    x: i32,
    y: i32,
}

const SIZE: usize = 10;

pub fn day9_2(str: &str) {
    let mut rope = [Node { x: 0, y: 0 }; SIZE];

    let mut visited = HashSet::new();

    for line in str.lines() {
        let mut split = line.split(" ");
        let dir = split.next().unwrap();
        let steps: i32 = split.next().unwrap().parse().unwrap();

        for _ in 0..steps {
            {
                let head = &mut rope[0];
                match dir {
                    "R" => {
                        head.x += 1;
                    }
                    "L" => {
                        head.x -= 1;
                    }
                    "U" => {
                        head.y -= 1;
                    }
                    "D" => {
                        head.y += 1;
                    }
                    other => {
                        panic!("unknown direction {}", other);
                    }
                }
            }
            for i in 0..(rope.len() - 1) {
                let head = rope[i];
                let mut tail = &mut rope[i + 1];
                let x_diff = head.x - tail.x;
                let y_diff = head.y - tail.y;
                if x_diff.abs() > 1 || y_diff.abs() > 1 {
                    tail.x += x_diff.signum();
                    tail.y += y_diff.signum();
                }
            }
            let tail = &rope[rope.len() - 1];
            visited.insert((tail.x, tail.y));
        }
    }

    println!("{}", visited.len());
}