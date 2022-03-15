//
//  PlayerTableViewCell.swift
//  VolleyballPassingStats
//
//  Created by Cody King on 4/26/20.
//  Copyright © 2022 Cody J. King. All rights reserved.
//

import UIKit

class PlayerTableViewCell: UITableViewCell, UITextFieldDelegate {

    // MARK: Properties
    
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var statsLabel: UILabel!
    @IBOutlet weak var lastPassLabel: UILabel!
    @IBOutlet weak var scoringBtn0: UIButton!
    @IBOutlet weak var scoringBtn1: UIButton!
    @IBOutlet weak var scoringBtn2: UIButton!
    @IBOutlet weak var scoringBtn3: UIButton!

        
    // the delegate, remember to set to weak to prevent cycles
    weak var delegate: PlayerTableViewCellDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        self.nameTextField.delegate = self
                
        // Add action to perform when the button is tapped
        self.scoringBtn0.addTarget(self, action: #selector(scoringButtonTapped(_:)), for: .touchUpInside)
        self.scoringBtn1.addTarget(self, action: #selector(scoringButtonTapped(_:)), for: .touchUpInside)
        self.scoringBtn2.addTarget(self, action: #selector(scoringButtonTapped(_:)), for: .touchUpInside)
        self.scoringBtn3.addTarget(self, action: #selector(scoringButtonTapped(_:)), for: .touchUpInside)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    
    // MARK: UITextFieldDelegate
    
    // Save name in textField when clicking another button or textField
    @IBAction func textFieldDone(_ textField: UITextField) {
        textFieldSaveHelper(textField)
    }

    // Save name in textField when clicking "done"
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textFieldSaveHelper(textField)
        
        return true
    }
    
    // Save name in textField for Player object
    func textFieldSaveHelper(_ textField: UITextField) {
        // Hide the keyboard.
        nameTextField.resignFirstResponder()
        
        // Get info to send to controller
        let cell = textField.superview?.superview as! UITableViewCell as UITableViewCell
        let table = cell.superview as! UITableView
        let indexPath = table.indexPath(for: cell)
        // row if the TableView
        let row = indexPath!.row
        
        let index = -1
        let task = "set name"
        
        let name = nameTextField.text ?? ""
        
        // ask the delegate (in most case, its the view controller) to
        // call the function 'scoringButtonTappedFor' on itself.
        if let _ = delegate {
            self.delegate?.playerTableViewCell(self, _: index, _: row, _: task, _: name)
        }
    }
     
    
    // MARK: Actions
    
    @IBAction func undoButtonTapped(_ sender: UIButton) {
        let buttonPosition = sender.convert(CGPoint(), to:sender.superview?.superview?.superview)
        // get TableView from button
        let view = sender.superview?.superview?.superview as! UITableView as UITableView
        let indexPath = view.indexPathForRow(at:buttonPosition)
        // row if the TableView
        let row = indexPath!.row
        
        let index = -1
        let task = "undo"
        
        if let _ = delegate {
            self.delegate?.playerTableViewCell(self, _: index, _: row, _: task, _: "")
        }
    }
    
    @IBAction func resetButtonTapped(_ sender: UIButton) {
        let buttonPosition = sender.convert(CGPoint(), to:sender.superview?.superview?.superview)
        // get TableView from button
        let view = sender.superview?.superview?.superview as! UITableView as UITableView 
        let indexPath = view.indexPathForRow(at:buttonPosition)
        // row if the TableView
        let row = indexPath!.row
        
        let index = -1
        let task = "reset"
        
        if let _ = delegate {
            self.delegate?.playerTableViewCell(self, _: index, _: row, _: task, _: "")
        }
    }
    
    @IBAction func scoringButtonTapped(_ sender: UIButton) {
        
        let buttonPosition = sender.convert(CGPoint(), to:sender.superview?.superview?.superview?.superview)
        // get TableView from button
        let view = sender.superview?.superview?.superview?.superview as! UITableView as UITableView
        let indexPath = view.indexPathForRow(at:buttonPosition)
        // row if the TableView
        let row = indexPath!.row
        
        // make sure the button's superview is a stack view
        guard let stack = sender.superview as? UIStackView else { return }
        
        // get the array of arranged subviews
        let theArray = stack.arrangedSubviews
        
        // get the "index" of the tapped button
        var index = theArray.firstIndex(of: sender)!
        
        var task = "score"
        
        // ask the delegate (in most case, its the view controller) to
        // call the function 'scoringButtonTappedFor' on itself.
        if let _ = delegate {
            self.delegate?.playerTableViewCell(self, _: index, _: row, _: task, _: "")
        }
        
        index = -1
        task = "set name"
        
        //for index in 0...19 {
            let name = nameTextField.text ?? ""
            
            //row = index
            
            // ask the delegate (in most case, its the view controller) to
            // call the function 'scoringButtonTappedFor' on itself.
            if let _ = delegate {
                self.delegate?.playerTableViewCell(self, _: index, _: row, _: task, _: name)
            }
        //}
    }
}

// Only class object can conform to this protocol (struct/enum can't)
protocol PlayerTableViewCellDelegate: AnyObject {
    func playerTableViewCell(_ playerTableViewCell: PlayerTableViewCell, _ index: Int, _ rowIndex: Int, _ task: String, _ name: String)
}
