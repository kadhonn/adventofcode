pub fn day2_1(str: &str) {
    let mut sum = 0;
    for line in str.lines() {
        let mut valid = true;
        let game_split = line.split(": ").collect::<Vec<&str>>();
        let game_number: i32 = game_split[0].split(" ").collect::<Vec<&str>>()[1].trim().parse().unwrap();
        let grabs_split = game_split[1].split("; ");
        for grab in grabs_split {
            let cubes_split = grab.split(", ");
            for cubes in cubes_split {
                let cubes_split = cubes.split(" ").collect::<Vec<&str>>();
                let num = cubes_split[0].trim().parse::<i32>().unwrap();
                let color = cubes_split[1].trim();
                if color == "red" {
                    valid &= num <= 12;
                } else if color == "green" {
                    valid &= num <= 13;
                } else if color == "blue" {
                    valid &= num <= 14;
                } else {
                    valid = false;
                }
            }
        }
        if valid {
            sum += game_number;
        }
    }
    println!("{}", sum);
}
