pub fn day5_1(str: &str) {
    let split: Vec<&str> = str.split("\r\n").collect();
    let (mut boxes, start_of_moves) = parse_boxes(&split);

    for i in (start_of_moves as usize)..(split.len()) {
        let line = split[i as usize];
        if line.len() == 0 {
            continue;
        }
        let (times, from, to) = parse_move(line);
        for _ in 0..times {
            let char = boxes[from as usize - 1].pop().unwrap();
            boxes[to as usize - 1].push(char);
        }
    }

    let mut result = String::from("");
    for i in 0..boxes.len() {
        result.push(boxes[i][boxes[i].len() - 1]);
    }

    println!("{}", result);
}
pub fn day5_2(str: &str) {
    let split: Vec<&str> = str.split("\r\n").collect();
    let (mut boxes, start_of_moves) = parse_boxes(&split);

    for i in (start_of_moves as usize)..(split.len()) {
        let line = split[i as usize];
        if line.len() == 0 {
            continue;
        }
        let (times, from, to) = parse_move(line);
        let mut tmp:Vec<char> = Vec::new();
        for _ in 0..times {
            let char = boxes[from as usize - 1].pop().unwrap();
            tmp.push(char);
        }
        tmp.reverse();
        boxes[to as usize - 1].append(&mut tmp);
    }

    let mut result = String::from("");
    for i in 0..boxes.len() {
        result.push(boxes[i][boxes[i].len() - 1]);
    }

    println!("{}", result);
}

fn parse_move(line: &str) -> (i32, i32, i32) {
    let chars: Vec<&str> = line.split(" ").collect();
    (chars[1].parse::<i32>().unwrap(),
     chars[3].parse::<i32>().unwrap(),
     chars[5].parse::<i32>().unwrap(), )
}

fn parse_boxes(split: &Vec<&str>) -> (Vec<Vec<char>>, i32) {
    let mut boxes: Vec<Vec<char>> = Vec::new();
    let mut i = 0;

    for _ in 0..(((split[0].len()) / 4) + 1) {
        boxes.push(Vec::new());
    }

    loop {
        if split[i + 1].len() == 0 {
            break;
        }
        let chars: Vec<char> = split[i].chars().collect();
        for j in 0..(split[i].len() / 4 + 1) {
            let char = chars[j * 4 + 1];
            if char != ' ' {
                boxes[j].push(char);
            }
        }


        i += 1;
    }

    for j in 0..boxes.len() {
        boxes[j].reverse();
    }

    (boxes, i as i32 + 2)
}